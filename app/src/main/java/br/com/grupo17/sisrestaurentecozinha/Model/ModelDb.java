package br.com.grupo17.sisrestaurentecozinha.Model;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Administracao;
import br.com.grupo17.sisrestaurentecozinha.Interfaces.Caixa;
import br.com.grupo17.sisrestaurentecozinha.Interfaces.Cardapio;
import br.com.grupo17.sisrestaurentecozinha.Interfaces.Conta;
import br.com.grupo17.sisrestaurentecozinha.Interfaces.MenuInter;
import br.com.grupo17.sisrestaurentecozinha.Interfaces.Pedidos;
import br.com.grupo17.sisrestaurentecozinha.ModeloObjeto.CardapioObj;
import br.com.grupo17.sisrestaurentecozinha.R;
import br.com.grupo17.sisrestaurentecozinha.Util.SpAndToast;

public class ModelDb implements Conta.ModelConta, MenuInter.ModelMenu, Pedidos.ModelPedido, Cardapio.ModelCardapio, Administracao.ModelAdministracao, Caixa.ModelCaixa {
    private Administracao.HorarioFuncionamentoPresenter horarioFuncionamentoPresenter;
    private Administracao.CadastroHorariosPresenter cadastroHorariosPresenter;
    private Administracao.RelatoriosVendasPresenter relatoriosVendasPresenter;
    private Administracao.CaixaRelatorioPresenter relatorioCaixaPresenter;
    private Administracao.GerenciamentoCupomPresenter cupomPresenter;
    private Administracao.HorarioEspecialPresenter especialPresenter;
    private Caixa.EscolherContasPresenter escolherContasPresenter;
    private Administracao.QuantidadeMesaPresenter mesaPresenter;
    private Conta.RecuperaSenhaPresenter recuperaSenhaPresenter;
    private Caixa.EscolherMesaPresenter escolherMesaPresenter;
    private Administracao.ReservaPresenter reservaPresenter;
    private Administracao.UsuarioPresenter usuarioPresenter;
    private Caixa.PedidosPresenter caixaPedidosPresenter;
    private Cardapio.CardapioPresenter cardapioPresenter;
    private Cardapio.CadastroPresenter cadastroPresenter;
    private Cardapio.DetalhePresenter detalhePresenter;
    private Caixa.AberturaPresenter aberturaPresenter;
    private Pedidos.PedidoPresenter pedidoPresenter;
    private Conta.LoginPresenter loginPresenter;
    private MenuInter.MenuPresenter menuPresenter;

    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private Activity activity;

    public ModelDb(Conta.LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    public ModelDb(Conta.RecuperaSenhaPresenter recuperaSenhaPresenter) {
        this.recuperaSenhaPresenter = recuperaSenhaPresenter;
    }

    public ModelDb(Pedidos.PedidoPresenter pedidoPresenter) {
        this.pedidoPresenter = pedidoPresenter;
    }

    public ModelDb(Cardapio.CardapioPresenter cardapioPresenter) {
        this.cardapioPresenter = cardapioPresenter;
    }

    public ModelDb(Cardapio.CadastroPresenter cadastroPresenter) {
        this.cadastroPresenter = cadastroPresenter;
    }

    public ModelDb(Cardapio.DetalhePresenter detalhePresenter) {
        this.detalhePresenter = detalhePresenter;
    }

    public ModelDb(Administracao.RelatoriosVendasPresenter relatoriosVendasPresenter) {
        this.relatoriosVendasPresenter = relatoriosVendasPresenter;
    }

    public ModelDb(Administracao.QuantidadeMesaPresenter mesaPresenter) {
        this.mesaPresenter = mesaPresenter;
    }

    public ModelDb(Administracao.ReservaPresenter reservaPresenter) {
        this.reservaPresenter = reservaPresenter;
    }

    public ModelDb(Administracao.HorarioFuncionamentoPresenter horarioFuncionamentoPresenter) {
        this.horarioFuncionamentoPresenter = horarioFuncionamentoPresenter;
    }

    public ModelDb(Caixa.EscolherMesaPresenter escolherMesaPresenter) {
        this.escolherMesaPresenter = escolherMesaPresenter;
    }

    public ModelDb(Administracao.UsuarioPresenter usuarioPresenter) {
        this.usuarioPresenter = usuarioPresenter;
    }

    public ModelDb(Caixa.EscolherContasPresenter escolherContasPresenter) {
        this.escolherContasPresenter = escolherContasPresenter;
    }

    public ModelDb(Administracao.HorarioEspecialPresenter especialPresenter) {
        this.especialPresenter = especialPresenter;
    }

    public ModelDb(Administracao.CadastroHorariosPresenter cadastroHorariosPresenter) {
        this.cadastroHorariosPresenter = cadastroHorariosPresenter;
    }

    public ModelDb(Caixa.PedidosPresenter caixaPedidosPresenter) {
        this.caixaPedidosPresenter = caixaPedidosPresenter;
    }

    public ModelDb(Caixa.AberturaPresenter aberturaPresenter) {
        this.aberturaPresenter = aberturaPresenter;
    }

    public ModelDb(Administracao.GerenciamentoCupomPresenter cupomPresenter) {
        this.cupomPresenter = cupomPresenter;
    }

    public ModelDb(MenuInter.MenuPresenter menuPresenter) {
        this.menuPresenter = menuPresenter;
    }

    public ModelDb(Administracao.CaixaRelatorioPresenter relatorioCaixaPresenter) {
        this.relatorioCaixaPresenter = relatorioCaixaPresenter;
    }

    /*
     * método para buscar itens de uma categoria especifica do cardapio
     */
    @Override
    public void buscarItens(String categoria) {
        getCollectionReferenceCardapio(categoria)
                .get()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        cardapioPresenter.responseBusca(queryDocumentSnapshots);
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        cardapioPresenter.setErro(e);
                    }
                });
    }

    /*
     * Cria a referencia do storage, usando o nome do arquivo, para ser usada no método DeletaFoto
     */
    @Override
    public void criaRefDeletaFotoComUri(Uri pathFoto) {
        if (pathFoto != null && pathFoto.getLastPathSegment() != null) {
            deletaFoto(getFirebaseStorage().getReference(pathFoto.getLastPathSegment()));
        }
    }

    /*
     * Cria a referencia do storage, usando a propria URL de download, para ser usada no método DeletaFoto
     */
    @Override
    public void criaRefDeletaFotoComUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            deletaFoto(getFirebaseStorage().getReferenceFromUrl(url));
        }
    }

    /*
     * Método usado para deletar a foto do Storage
     */
    private void deletaFoto(StorageReference storageReference) {
        if (storageReference != null) {
            storageReference.delete().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (cadastroPresenter != null) {
                        cadastroPresenter.responseDeletaFoto(task);
                    } else if (detalhePresenter != null) {
                        detalhePresenter.responseDelete(R.string.key_campo_foto, task);
                    }
                }
            });
        }
    }

    /*
     * Método usado para deletar os horarios de funcionamento do estabelecimento, tanto os normais e os especiais.
     */
    @Override
    public void deleteHorario(String dia, String horario, final boolean especial, boolean ultimoItem) {
        if (especial) {
            DocumentReference refDia = getDocumentReferenceConfiguracao(getString(R.string.key_path_db_horarios_espaciais))
                    .collection(getString(R.string.key_path_subDb_datas))
                    .document(dia);

            DocumentReference refHor = refDia.collection(getString(R.string.key_path_subDb_horario))
                    .document(horario);
            if (ultimoItem) {
                //Usando Gravação em Lote;
                WriteBatch processolote = getFirestore().batch();
                processolote.delete(refHor);
                processolote.delete(refDia);
                processolote.commit()
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                especialPresenter.responseDelete(task);
                            }
                        });
            } else {
                refHor.delete()
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                especialPresenter.responseDelete(task);
                            }
                        });
            }
        } else {
            //Delete convencional
            getDocumentReferenceConfiguracao(getString(R.string.key_path_db_horario_funcionamento))
                    .collection(dia.toLowerCase())
                    .document(horario)
                    .delete()
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            horarioFuncionamentoPresenter.responseDelete(task);
                        }
                    });
        }
    }

    /*
     * Método usado para deletar o um item de uma categoria do cardapio.
     */
    @Override
    public void deletaItem(String categoria, String idItem) {
        getCollectionReferenceCardapio(categoria)
                .document(idItem)
                .delete().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (cadastroPresenter != null) {
                    cadastroPresenter.responseDeletaItem(task);
                } else {
                    detalhePresenter.responseDelete(R.string.key_item, task);
                }
            }
        });
    }

    /*
     * Método usado para deslogar o usuario e limpar o nivel do usuario do sharedpreferences.
     */
    @Override
    public void deslogar() {
        getAuth().signOut();
        SpAndToast.resetSP(getActivity());
    }

    /*
     * Método usado para setar o valor na variavel activity, com base na tela que esta aberta;
     */
    @NonNull
    private Activity getActivity() {
        if (loginPresenter != null) {
            activity = (Activity) loginPresenter.getContext();
        } else if (recuperaSenhaPresenter != null) {
            activity = (Activity) recuperaSenhaPresenter.getContext();
        } else if (cardapioPresenter != null) {
            activity = (Activity) cardapioPresenter.getContext();
        } else if (detalhePresenter != null) {
            activity = (Activity) detalhePresenter.getContext();
        } else if (cadastroPresenter != null) {
            activity = (Activity) cadastroPresenter.getContext();
        } else if (pedidoPresenter != null) {
            activity = (Activity) pedidoPresenter.getContext();
        } else if (relatoriosVendasPresenter != null) {
            activity = (Activity) relatoriosVendasPresenter.getContext();
        } else if (mesaPresenter != null) {
            activity = (Activity) mesaPresenter.getContext();
        } else if (reservaPresenter != null) {
            activity = (Activity) reservaPresenter.getContext();
        } else if (horarioFuncionamentoPresenter != null) {
            activity = (Activity) horarioFuncionamentoPresenter.getContext();
        } else if (escolherMesaPresenter != null) {
            activity = (Activity) escolherMesaPresenter.getContext();
        } else if (usuarioPresenter != null) {
            activity = (Activity) usuarioPresenter.getContex();
        } else if (especialPresenter != null) {
            activity = (Activity) especialPresenter.getContext();
        } else if (escolherContasPresenter != null) {
            activity = (Activity) escolherContasPresenter.getContext();
        } else if (cadastroHorariosPresenter != null) {
            activity = (Activity) cadastroHorariosPresenter.getContext();
        } else if (caixaPedidosPresenter != null) {
            activity = (Activity) caixaPedidosPresenter.getContext();
        } else if (aberturaPresenter != null) {
            activity = (Activity) aberturaPresenter.getContext();
        } else if (cupomPresenter != null) {
            activity = (Activity) cupomPresenter.getContext();
        } else if (menuPresenter != null) {
            activity = (Activity) menuPresenter.getContext();
        } else if (relatorioCaixaPresenter != null) {
            activity = (Activity) relatorioCaixaPresenter.getContext();
        }
        return (activity);
    }

    /*
     * Método usado para instanciar a variavel auth; Variavel da Autenticação;
     */
    private FirebaseAuth getAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return (auth);
    }

    /*
     * Método usado para criar parte da referencia ao nó do cardapio, usando a categoria;
     * Criamos esse método porque usamos em mais de um método essa referencia.
     */
    private CollectionReference getCollectionReferenceCardapio(String categoria) {
        return getFirestore()
                .collection(getString(R.string.key_path_db_cad))
                .document(categoria.toLowerCase())
                .collection(getString(R.string.key_path_db_cad_itens));
    }

    /*
     * Método usado para criar parte da referencia ao nó dos usuarios;
     * Criamos esse método porque usamos em mais de um método essa referencia.
     */
    private CollectionReference getCollectionReferenceUsuario() {
        return getFirestore().collection(getString(R.string.key_path_db_usuarios));
    }

    /*
     * Método usado para criar parte da referencia ao nó dos usuarios;
     * Criamos esse método porque usamos em mais de um método essa referencia.
     */
    @Override
    public CollectionReference getCollectionReferencePedidos() {
        return getFirestore().collection(getString(R.string.key_path_db_pedidos));
    }

    private CollectionReference getCollectionReferenceCaixaUsuario() {
        return getFirestore()
                .collection("caixa")
                .document("aberturaefechamento")
                .collection("usuarios");
    }

    private CollectionReference getCollectionReferenceCupomDesconto() {
        return getFirestore().collection("cupomdedesconto");
    }

    private CollectionReference getCollectionMesasClientes(String mesa) {
        CollectionReference base = getFirestore().collection("mesas");
        return TextUtils.isEmpty(mesa) ?
                base.document(SpAndToast.getSP(getActivity(), "mesa"))
                        .collection("clientes") : base.document(mesa).collection("clientes");
    }

    private DocumentReference getDocumentReferenceConfiguracao(String document) {
        return getFirestore()
                .collection(getString(R.string.key_path_db_configuracoes))
                .document(document);
    }

    private FirebaseFirestore getFirestore() {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
        }
        return (firestore);
    }

    private FirebaseStorage getFirebaseStorage() {
        if (storage == null) {
            storage = FirebaseStorage.getInstance();
        }
        return (storage);
    }

    private String getString(int string) {
        return getActivity().getString(string);
    }

    @Override
    public boolean isLoggedIn() {
        return getAuth().getCurrentUser() != null;
    }

    @Override
    public void login(String email, String password) {
        getAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                loginPresenter.responseLogin(task);
            }
        });
    }

    @Override
    public void retrivePedidos() {
        getCollectionReferencePedidos()
                .whereEqualTo(getString(R.string.key_campo_statusitem), getString(R.string.key_status_cliente_pediu))
                .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        pedidoPresenter.responseRetrieve(queryDocumentSnapshots);
                    }
                });

        getCollectionReferencePedidos()
                .whereEqualTo(getString(R.string.key_campo_statusitem), getString(R.string.key_status_cozinha_preparando))
                .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        pedidoPresenter.responseRetrieve(queryDocumentSnapshots);
                    }
                });
    }

    @Override
    public void retriveDadosRelatorios(Query query) {
        query.get().addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                relatoriosVendasPresenter.responseDados(task);
            }
        });
    }

    @Override
    public void retriveReserva() {
        getDocumentReferenceConfiguracao(getString(R.string.key_path_db_configuracoes_reservas))
                .get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        reservaPresenter.responseReserva(task);
                    }
                });
    }

    @Override
    public void retriveUsuario(String cpf) {
        getCollectionReferenceUsuario()
                .whereEqualTo(getString(R.string.key_campo_cpf), cpf)
                .get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        usuarioPresenter.responseUsuario(task);
                    }
                });
    }

    @Override
    public void resetaMesa(String userId) {
        getCollectionReferenceUsuario()
                .document(userId)
                .update(getString(R.string.key_campo_mesa), getString(R.string.key_item_nao_disponivel_cardapio))
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        usuarioPresenter.responseReset(task);
                    }
                });
    }

    @Override
    public void retriveQuantidadeMesa() {
        getDocumentReferenceConfiguracao(getString(R.string.key_path_db_configuracoes_mesas_e_reservas))
                .get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        mesaPresenter.responseRetriveQuantidade(task);
                    }
                });
    }

    @Override
    public void recuperarSenha(String email) {
        getAuth().sendPasswordResetEmail(email).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                recuperaSenhaPresenter.responseRecuperar(task);
            }
        });
    }

    @Override
    public void retriveHorariosFuncionamentos(String dias) {
        getDocumentReferenceConfiguracao(getString(R.string.key_path_db_horario_funcionamento))
                .collection(dias.toLowerCase())
                .get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        horarioFuncionamentoPresenter.responseHorarios(task);
                    }
                });
    }

    @Override
    public void retriveDiasEspeciais() {
        getDocumentReferenceConfiguracao(getString(R.string.key_path_db_horarios_espaciais))
                .collection(getString(R.string.key_path_subDb_datas))
                .get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        especialPresenter.responseDiasEspeciais(task);
                    }
                });
    }

    @Override
    public void retriveHorarios(String dia) {
        getDocumentReferenceConfiguracao(getString(R.string.key_path_db_horarios_espaciais))
                .collection(getString(R.string.key_path_subDb_datas))
                .document(dia)
                .collection(getString(R.string.key_path_subDb_horario))
                .get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        especialPresenter.responseHorarios(task);
                    }
                });
    }

    @Override
    public void updateCampoPathFoto(String categoria, String idItem, String path) {
        getCollectionReferenceCardapio(categoria)
                .document(idItem)
                .update(getString(R.string.key_campo_foto), path)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        cadastroPresenter.responseUpdateCampoPathFoto(task);
                    }
                });
    }

    @Override
    public void updateStatus(String status, String idItem) {
        getCollectionReferencePedidos()
                .document(idItem)
                .update(getString(R.string.key_campo_statusitem), status)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pedidoPresenter.responseUpdate(task);
                    }
                });
    }

    @Override
    public void updateItem(CardapioObj cardapioObj) {
        getCollectionReferenceCardapio(cardapioObj.getCategoria())
                .document(cardapioObj.getId())
                .update(cardapioObj.getMap(cadastroPresenter.getContext()))
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        cadastroPresenter.responseUpdateItem(task);
                    }
                });
    }

    @Override
    public void updateQuantidadeMesa(String qtMesa) {
        getDocumentReferenceConfiguracao(getString(R.string.key_path_db_configuracoes_mesas_e_reservas))
                .update(getString(R.string.key_campo_qtMesa_total), qtMesa)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mesaPresenter.responseUpdateQtMesa(task);
                    }
                });
    }

    @Override
    public void updateReserva(Map<String, Object> map) {
        getDocumentReferenceConfiguracao(getString(R.string.key_path_db_configuracoes_reservas))
                .update(map)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        reservaPresenter.responseAlterar(task);
                    }
                });
    }

    @Override
    public void updateNivelUser(String idUser, String nivelUser) {
        getCollectionReferenceUsuario()
                .document(idUser)
                .update(getString(R.string.key_campo_nivelUser), nivelUser)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        usuarioPresenter.responseUpdate(task);
                    }
                });
    }

    @Override
    public void salvaCardapioObj(CardapioObj cardapioObj) {
        getCollectionReferenceCardapio(cardapioObj.getCategoria())
                .add(cardapioObj.getMap(cadastroPresenter.getContext()))
                .addOnCompleteListener(getActivity(), new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        cadastroPresenter.responseSalvaItem(task);
                    }
                });

    }

    @Override
    public void salvaFotoItem(Uri uri, String categoria) {
        if (uri != null && uri.getLastPathSegment() != null) {
            final StorageReference storageReference = getFirebaseStorage().getReference().child(getString(R.string.key_path_db_cad)).child(categoria.toLowerCase() + "/" + uri.getLastPathSegment());
            UploadTask uploadTask = storageReference.putFile(Uri.parse("file://" + uri.toString()));
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(getActivity(), new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    cadastroPresenter.responseSaltaFoto(task);
                }
            });
        }
    }

    @Override
    public void salvaHorarios(final int configuracao, final Boolean finaliza, String dia, HashMap<String, Object> map) {
        if (configuracao == R.string.key_path_db_horarios_espaciais) {
            WriteBatch processolote = getFirestore().batch();

            DocumentReference refDia = getDocumentReferenceConfiguracao(getString(configuracao)).collection(getString(R.string.key_path_subDb_datas)).document(dia);
            if (finaliza) {
                Map<String, Object> temp = new HashMap<>();
                temp.put(getString(R.string.key_campo_data), dia);
                processolote.set(refDia, temp);
            }

            DocumentReference refHorario = refDia.collection(getString(R.string.key_path_subDb_horario)).document(Objects.requireNonNull(map.get(getString(R.string.campo_hora))).toString());
            processolote.set(refHorario, map);

            processolote.commit().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (finaliza || task.getException() != null) {
                        cadastroHorariosPresenter.responseHorarios(task);
                    }
                }
            });
        } else {
            getDocumentReferenceConfiguracao(getString(configuracao))
                    .collection(dia.toLowerCase())
                    .document(Objects.requireNonNull(map.get(getString(R.string.campo_hora))).toString())
                    .set(map).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (finaliza || task.getException() != null) {
                        cadastroHorariosPresenter.responseHorarios(task);
                    }
                }
            });
        }
    }

    @Override
    public void verificaNivelUsuario() {
        if (getAuth().getUid() != null) {
            getCollectionReferenceUsuario()
                    .document(getAuth().getUid())
                    .get()
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            loginPresenter.responseVerificacaoNivelUser(task);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loginPresenter.responseVerificacaoNivelUser(null);
                        }
                    });
        }
    }

    @Override
    public void nivelUserRealTime() {
        if (getAuth().getUid() != null) {
            getCollectionReferenceUsuario()
                    .document(getAuth().getUid())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }

                            menuPresenter.responseNivelUser(getActivity(), documentSnapshot);
                        }
                    });
        }
    }

    @Override
    public void verificaPedidosAbertos(String uidItem) {
        getCollectionReferencePedidos()
                .document(uidItem)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        escolherContasPresenter.responseVerificaPedidos(task);
                    }
                });
    }

    @Override
    public void fechaContas(String uID) {
        getCollectionReferenceUsuario()
                .document(uID)
                .update("mesa", "fechada")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        escolherContasPresenter.responseListfechaConta(task);
                    }
                });
    }

    @Override
    public String obterUidCaixa() {
        return getAuth().getUid();
    }

    @Override
    public void salvaVenda(HashMap<String, Object> map) {
        getFirestore()
                .collection("caixa")
                .document("vendas")
                .collection("comprovantes")
                .add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        caixaPedidosPresenter.responseVenda(task);
                    }
                });
    }

    @Override
    public void mudaStatusPedidos(String pedidos) {
        getCollectionReferencePedidos()
                .document(pedidos)
                .update("statusitem", "Aguardando Avaliação")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        caixaPedidosPresenter.responseMudarStatus(task);
                    }
                });
    }

    @Override
    public void buscaPedidos(String mesa, String uID) {
        getCollectionMesasClientes(mesa)
                .document(uID)
                .collection("pedidos")
                .get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        escolherContasPresenter.responseListPedidos(task);
                    }
                });
    }

    @Override
    public void consultaMesa(final String mesa) {
        getCollectionMesasClientes(mesa)
                .get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        escolherMesaPresenter.responseConsultaMesa(task, mesa);
                    }
                });
    }

    @Override
    public void buscaNomeContas() {
        getCollectionMesasClientes(null)
                .get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        escolherContasPresenter.responseNomesContas(task);
                    }
                });
    }

    @Override
    public void excluiPedidoMesa(final String uid) {
        getCollectionMesasClientes(null)
                .document(uid)
                .collection("pedidos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        caixaPedidosPresenter.responseExcluiPedidoMesa(task, uid);
                    }
                });
    }

    @Override
    public void excluiItens(String uidIten, final String uid) {
        getCollectionMesasClientes(null)
                .document(uid)
                .collection("pedidos")
                .document(uidIten)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        caixaPedidosPresenter.responseExcluiItens(task, uid);
                    }
                });
    }

    @Override
    public void excluiUid(String uid) {
        getCollectionMesasClientes(null)
                .document(uid)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        caixaPedidosPresenter.responseExcluiUid(task);
                    }
                });
    }

    @Override
    public void consultaCodigoDesconto(final String codigo) {
        getCollectionReferenceCupomDesconto()
                .document(codigo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        caixaPedidosPresenter.responseCodigoDesconto(task, codigo);
                    }
                });
    }

    @Override
    public void salvaLimiteDesconto(Float limite, String codigo) {
        getCollectionReferenceCupomDesconto()
                .document(codigo)
                .update("limite", limite)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        caixaPedidosPresenter.responseLimiteDesconto(task);
                    }
                });
    }

    @Override
    public void salvaCupom(Map<String, Object> map) {
        getCollectionReferenceCupomDesconto()
                .document(Objects.requireNonNull(map.get("nome")).toString())
                .set(map)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        cupomPresenter.responseSalvaCupom(task);
                    }
                });
    }

    @Override
    public void retriveCupons() {
        getCollectionReferenceCupomDesconto()
                .get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        cupomPresenter.responseRetriveCupons(task);
                    }
                });
    }

    @Override
    public void deletaCupom(String cupom) {
        getCollectionReferenceCupomDesconto()
                .document(cupom)
                .delete()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        cupomPresenter.responseDeleteCupon(task);
                    }
                });
    }

    @Override
    public void buscaDadosCaixa() {
        getCollectionReferenceUsuario()
                .document(obterUidCaixa())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        aberturaPresenter.responseBuscaUidCaixa(task);
                    }
                });
    }

    @Override
    public void verificaCaixaAberto() {
        getCollectionReferenceCaixaUsuario()
                .document(obterUidCaixa())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        aberturaPresenter.responseVerificaCaixaAberto(task);
                    }
                });
    }

    @Override
    public void abreCaixa(Map<String, Object> abertura) {
        getCollectionReferenceCaixaUsuario()
                .document(obterUidCaixa())
                .set(abertura)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        aberturaPresenter.responseAbreCaixa(task);
                    }
                });
    }

    @Override
    public void fecharCaixa() {
        getCollectionReferenceCaixaUsuario()
                .document(obterUidCaixa())
                .update("status", "fechado")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        escolherMesaPresenter.responseFecharCaixa(task);
                    }
                });
    }

    @Override
    public void retriveDadosCaixas() {
        getCollectionReferenceCaixaUsuario()
                .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            SpAndToast.showMessage(getActivity(), e.getMessage());
                            return;
                        }
                        relatorioCaixaPresenter.responseDados(queryDocumentSnapshots);
                    }
                });
    }

    @Override
    public void buscaValorFechamento() {
        getCollectionReferenceCaixaUsuario()
                .document(obterUidCaixa())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        caixaPedidosPresenter.responseBuscaValorFechamento(task);
                    }
                });
    }

    @Override
    public void salvaValorFechamento(Float valor) {
        getCollectionReferenceCaixaUsuario()
                .document(obterUidCaixa())
                .update("valorfechamento", valor)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        caixaPedidosPresenter.responseSalvaValorFechamento(task);
                    }
                });
    }

    @Override
    public void updateStatusCaixa(String id, String status) {
        getCollectionReferenceCaixaUsuario()
                .document(id)
                .update("status", status)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        relatorioCaixaPresenter.responseStatus(task);
                    }
                });
    }

    @Override
    public void deletaItemCaixa(String id) {
        getCollectionReferenceCaixaUsuario()
                .document(id)
                .delete()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        relatorioCaixaPresenter.responseDelete(task);
                    }
                });
    }
}